// noinspection SqlResolve

import {Service} from "typedi";
import {NodePgTemplate} from "../../config/database/NodePgTemplate";
import {Student} from "../../entity/student/Student";
import {BaseRepository} from "../BaseRepository";

@Service()
export class StudentRepository extends BaseRepository<Student> {

    constructor(nodePgTemplate: NodePgTemplate) {
        super(nodePgTemplate, Student);
    }

    async exist (email: string): Promise<boolean> {
        const sql = `SELECT 1 FROM student WHERE email= '${email}' limit 1`;
        try {
            const rows = await this.nodePgTemplate.query(sql);
            return rows[0] === 1;
        } catch (e) {
            console.error(`해당 Email 조회에 실패하였습니다. email=${email}`, e);
            throw new Error(e.message);
        }
    }
}
