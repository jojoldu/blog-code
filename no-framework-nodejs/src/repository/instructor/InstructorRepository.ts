// noinspection SqlResolve

import {Service} from "typedi";
import dayjs from "dayjs";
import {NodePgTemplate} from "../../config/database/NodePgTemplate";
import {BaseRepository} from "../BaseRepository";
import {Instructor} from "../../entity/instructor/Instructor";

@Service()
export class InstructorRepository extends BaseRepository<Instructor>{

    constructor(nodePgTemplate: NodePgTemplate) {
        super(nodePgTemplate, Instructor);
    }

    async now(): Promise<string> {
        const sql = "SELECT NOW()";
        try {
            const rows = await this.nodePgTemplate.query(sql);
            return dayjs(rows[0].now).format('YYYY-MM-DD HH:mm:ss');
        } catch (e) {
            throw new Error(e.message);
        }
    }

    async exist(email: string): Promise<boolean> {
        const sql = `SELECT 1 FROM instructor WHERE email= '${email}' limit 1`;
        try {
            const rows = await this.nodePgTemplate.query(sql);
            return rows[0] === 1;
        } catch (e) {
            console.error(`해당 Email 조회에 실패하였습니다. email=${email}`, e);
            throw new Error(e.message);
        }
    }
}
