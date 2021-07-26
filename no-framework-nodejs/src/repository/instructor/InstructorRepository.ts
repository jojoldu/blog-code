import {Service} from "typedi";
import dayjs from "dayjs";
import {NodeTemplate} from "../../config/database/NodeTemplate";

@Service()
export class InstructorRepository {

    constructor(private nodeTemplate: NodeTemplate) {}

    async now(): Promise<string> {
        const sql = "SELECT NOW()";
        try {
            const rows = await this.nodeTemplate.query(sql);
            const formatNow = dayjs(rows[0].now).format('YYYY-MM-DD HH:mm:ss');
            console.debug(`formatNow=${formatNow}`);
            return formatNow;
        } catch (error) {
            throw new Error(error.message);
        }
    }
}
