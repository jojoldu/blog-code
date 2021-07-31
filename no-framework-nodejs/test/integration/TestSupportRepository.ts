import {Service} from "typedi";
import {NodePgTemplate} from "../../src/config/database/NodePgTemplate";
import * as fs from "fs";

@Service()
export class TestSupportRepository {

    constructor(private nodePgTemplate: NodePgTemplate) {}

    async createTable(): Promise<void> {
        const sql = fs.readFileSync('sql/init.sql').toString();
        await this.nodePgTemplate.query(sql);
    }

    async importData(): Promise<void> {
        const sql = fs.readFileSync('sql/data.sql').toString();
        await this.nodePgTemplate.query(sql);
    }

    async dropAll(): Promise<void> {
        await this.nodePgTemplate.query("DROP TABLE IF EXISTS student_lecture_map");
        await this.nodePgTemplate.query("DROP TABLE IF EXISTS student");
        await this.nodePgTemplate.query("DROP TABLE IF EXISTS lecture");
    }

    /**
     * Test DB의 데이터를 모두 초기화한다.
     */
    async deleteAll(): Promise<void> {
        await this.nodePgTemplate.query("DELETE FROM student_lecture_map");
        await this.nodePgTemplate.query("DELETE FROM student");
        await this.nodePgTemplate.query("DELETE FROM lecture");
    }
}
