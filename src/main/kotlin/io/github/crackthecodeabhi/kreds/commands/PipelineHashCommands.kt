package io.github.crackthecodeabhi.kreds.commands

import io.github.crackthecodeabhi.kreds.pipeline.QueuedCommand
import io.github.crackthecodeabhi.kreds.pipeline.Response

public interface PipelineHashCommands{
    /**
     * @see [HashCommands.hdel]
     */
    public suspend fun hdel(key: String, field: String, vararg moreFields: String): Response<Long>

    /**
     * @see [HashCommands.hexists]
     */
    public suspend fun hexists(key: String, field: String): Response<Long>

    /**
     * @see [HashCommands.hget]
     */
    public suspend fun hget(key: String, field: String): Response<String?>

    /**
     * @see [HashCommands.hgetAll]
     */
    public suspend fun hgetAll(key: String): Response<List<String>>

    /**
     * @see [HashCommands.hincrBy]
     */
    public suspend fun hincrBy(key: String, field: String, increment: Long): Response<Long>

    /**
     * @see [HashCommands.hincrByFloat]
     */
    public suspend fun hincrByFloat(key: String,field: String, increment: Long): Response<String>

    /**
     * @see [HashCommands.hkeys]
     */
    public suspend fun hkeys(key: String): Response<List<String>>

    /**
     * @see [HashCommands.hlen]
     */
    public suspend fun hlen(key: String): Response<Long>

    /**
     * @see [HashCommands.hmget]
     */
    public suspend fun hmget(key: String, field: String, vararg fields: String): Response<List<String?>>

    /**
     * @see [HashCommands.hrandfield]
     */
    public suspend fun hrandfield(key: String): Response<String?>


    /**
     * @see [HashCommands.hrandfield]
     */
    public suspend fun hrandfield(key: String, count: Int, withValues: Boolean? = null): Response<List<String>>


    /**
     * @see [HashCommands.hset]
     */
    public suspend fun hset(key: String, fieldValuePair: Pair<String,String>, vararg fieldValuePairs: Pair<String,String>): Response<Long>

    /**
     * @see [HashCommands.hsetnx]
     */
    public suspend fun hsetnx(key: String,field: String, value: String): Response<Long>

    /**
     * @see [HashCommands.hstrlen]
     */
    public suspend fun hstrlen(key: String, field: String): Response<Long>

     /**
     * @see [HashCommands.hvals]
     */
     public suspend fun hvals(key: String): Response<List<String>>

    /**
     * @see  [HashCommands.hscan]
     */
    public suspend fun hscan(key: String, cursor: Long, matchPattern: String? = null, count: Long? = null): Response<HScanResult>
}

internal interface PipelineHashCommandExecutor: QueuedCommand, PipelineHashCommands, BaseHashCommands {
    override suspend fun hdel(key: String, field: String, vararg moreFields: String): Response<Long> =
        add(_hdel(key,field, *moreFields))

    override suspend fun hexists(key: String, field: String): Response<Long> =
        add(_hexists(key, field))

    override suspend fun hget(key: String, field: String): Response<String?> =
        add(_hget(key, field))

    override suspend fun hgetAll(key: String): Response<List<String>> =
        add(_hgetAll(key))

    override suspend fun hincrBy(key: String, field: String, increment: Long): Response<Long> =
        add(_hincrBy(key, field, increment))

    override suspend fun hincrByFloat(key: String, field: String, increment: Long): Response<String> =
        add(_hincrByFloat(key, field, increment))

    override suspend fun hkeys(key: String): Response<List<String>> =
        add(_hkeys(key))

    override suspend fun hlen(key: String): Response<Long> =
        add(_hlen(key))

    override suspend fun hmget(key: String, field: String, vararg fields: String): Response<List<String?>> =
        add(_hmget(key, field, *fields))

    override suspend fun hrandfield(key: String): Response<String?> =
        add(_hrandfield(key))

    override suspend fun hrandfield(key: String, count: Int, withValues: Boolean?): Response<List<String>> =
        add(_hrandfield(key, count, withValues))

    override suspend fun hset(
        key: String,
        fieldValuePair: Pair<String,String>,
        vararg fieldValuePairs: Pair<String,String>
    ): Response<Long> =
        add(_hset(key, fieldValuePair, *fieldValuePairs))

    override suspend fun hsetnx(key: String, field: String, value: String): Response<Long> =
        add(_hsetnx(key, field, value))

    override suspend fun hstrlen(key: String, field: String): Response<Long> =
        add(_hstrlen(key, field))

    override suspend fun hvals(key: String): Response<List<String>> =
        add(_hvals(key))

    override suspend fun hscan(key: String, cursor: Long, matchPattern: String?, count: Long?): Response<HScanResult> =
        add(_hscan(key, cursor, matchPattern, count))
}