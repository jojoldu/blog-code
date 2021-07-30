import {Pool, PoolClient, QueryResult} from "pg";
import {dbProperties} from "./pgConfig";
import {Service} from "typedi";

@Service()
export class NodeTemplate {
    pool: Pool;

    constructor() {
        this.pool = new Pool(dbProperties);

        this.pool.on('error', (e: Error) => {
            console.error(`idle client error= ${e.message}`, e);
        });

    }

    async query(sql: string): Promise<any[]> {
        try {
            const result: QueryResult = await this.pool.query(sql);
            console.debug(`query(): query=${sql}, resultCount=${result.rowCount}`);
            return result.rows;
        } catch (e) {
            console.error(`query(): query=${sql}`, e);
            throw new Error(e.message);
        }
    }

    async startTransaction(): Promise<PoolClient> {
        console.debug(`startTransaction()`);
        const client: PoolClient = await this.pool.connect();
        try {
            await client.query('BEGIN');
            return client;
        } catch (e) {
            console.error('Transaction 생성 실패', e);
            throw new Error(e.message);
        }
    }

    async rollback(client: PoolClient) {
        if (typeof client == 'undefined' || !client) {
            console.warn(`rollback() 유효하지 않은 PoolClient`);
            return;
        }

        try {
            console.info(`sql transaction rollback`);
            await client.query('ROLLBACK');
        } catch (e) {
            console.error('rollback 실패', e);
            throw new Error(e.message);
        } finally {
            client.release();
        }
    }

    async commit (client: PoolClient) {
        console.debug(`transaction committed`);
        try {
            await client.query('COMMIT');
        } catch (e) {
            console.error('commit 실패', e);
            throw new Error(e.message);
        } finally {
            client.release();
        }
    }
}




