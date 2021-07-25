import {Pool, PoolClient, QueryResult} from "pg";
import {dbProperties} from "./pgConfig";

const pool = new Pool(dbProperties);

pool.on('error', (e: Error) => {
    console.error(`idle client error= ${e.message}`, e);
});

export const query = async (sql: string) => {
    try {
        const result:QueryResult = await pool.query(sql);
        console.debug(`query(): ${result.command} | ${result.rowCount}`);
        return result;
    } catch (e) {
        throw new Error(e.message);
    }
}

export const startTransaction = async () => {
    console.debug(`getTransaction()`);
    const client: PoolClient = await pool.connect();
    try {
        await client.query('BEGIN');
        return client;
    } catch (e) {
        throw new Error(e.message);
    }
}

export const rollback = async (client: PoolClient) => {
    if (typeof client !== 'undefined' && client) {
        try {
            console.info(`sql transaction rollback`);
            await client.query('ROLLBACK');
        } catch (e) {
            throw new Error(e.message);
        } finally {
            client.release();
        }
    } else {
        console.warn(`rollback() 실패`);
    }
}

export const commit = async (client: PoolClient) => {
    console.debug(`transaction committed`);
    try {
        await client.query('COMMIT');
    } catch (e) {
        throw new Error(e.message);
    } finally {
        client.release();
    }
}

