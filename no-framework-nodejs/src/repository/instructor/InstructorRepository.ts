import {Service} from "typedi";
import dayjs from "dayjs";
import {NodePgTemplate} from "../../config/database/NodePgTemplate";

@Service()
export class InstructorRepository {

    constructor(private nodePgTemplate: NodePgTemplate) {}

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
