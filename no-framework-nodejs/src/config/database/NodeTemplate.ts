import {PoolClient} from "pg";

export interface NodeTemplate {
    init(): void;

    query(sql: string): Promise<any[]>;

    startTransaction(): Promise<PoolClient>;

    rollback(client: PoolClient);

    commit(client: PoolClient);
}
