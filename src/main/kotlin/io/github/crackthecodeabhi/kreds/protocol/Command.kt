package io.github.crackthecodeabhi.kreds.protocol

import io.netty.handler.codec.redis.*
import io.github.crackthecodeabhi.kreds.args.*
import io.github.crackthecodeabhi.kreds.commands.*
import io.github.crackthecodeabhi.kreds.toByteBuf
import kotlin.jvm.Throws

internal fun Command.toRedisMessageList(): List<FullBulkStringRedisMessage> {
    return if(subCommand != null){
         listOf(FullBulkStringRedisMessage(string.toByteBuf()),*subCommand!!.toRedisMessageList().toTypedArray())
    } else listOf(FullBulkStringRedisMessage(string.toByteBuf()))
}

internal interface ICommandProcessor {
    fun encode(command: Command, vararg args: Argument): RedisMessage
    @Throws(KredsRedisDataException::class)
    fun <T> decode(message: RedisMessage): T
}

internal interface CommandExecutor {
    suspend fun <T> execute(command: Command, processor: ICommandProcessor, vararg args: Argument): T
    suspend fun <T> execute(commandExecution: CommandExecution): T
    suspend fun executeCommands(commands: List<CommandExecution>): List<RedisMessage>
}

internal val IntegerCommandProcessor = CommandProcessor(IntegerHandler)
internal val BulkStringCommandProcessor = CommandProcessor(BulkStringHandler)
internal val SimpleStringCommandProcessor = CommandProcessor(SimpleStringHandler)
internal val ArrayCommandProcessor = CommandProcessor(ArrayHandler)

internal open class CommandProcessor(private vararg val outputTypeHandlers: MessageHandler<*>): ICommandProcessor {

    override fun encode(command: Command,vararg args: Argument): RedisMessage {
        if(args.isEmpty()) return ArrayRedisMessage(command.toRedisMessageList())
        val x = command.toRedisMessageList().toMutableList()
        x.addAll(args.map { FullBulkStringRedisMessage(it.toString().toByteBuf()) })
        return ArrayRedisMessage(x as List<RedisMessage>)
    }

    @Throws(KredsRedisDataException::class)
    @Suppress("UNCHECKED_CAST")
    override fun <T> decode(message:RedisMessage): T {
        if(message is ErrorRedisMessage) throw KredsRedisDataException(message.content())
        val handler = outputTypeHandlers.first { it.canHandle(message) }
        return handler.doHandle(message) as T
    }
}