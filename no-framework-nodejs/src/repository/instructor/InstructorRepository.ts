import {Service} from "typedi";
import dayjs from "dayjs";
import {NodePgTemplate} from "../../config/database/NodePgTemplate";
import {BaseRepository} from "../BaseRepository";
import {Lecture} from "../../entity/lecture/Lecture";
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
            const formatNow = dayjs(rows[0].now).format('YYYY-MM-DD HH:mm:ss');
            console.debug(`formatNow=${formatNow}`);
            return formatNow;
        } catch (error) {
            throw new Error(error.message);
        }
    }
}
