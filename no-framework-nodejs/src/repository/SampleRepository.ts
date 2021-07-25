import {query} from "../config/database/nodePgTemplate";
import {Service} from "typedi";
import dayjs from "dayjs";

@Service()
export class SampleRepository {

    async now(): Promise<string> {
        const sql = "SELECT NOW()";
        try {
            const queryResult = await query(sql);
            const formatNow = dayjs(queryResult.rows[0].now).format('YYYY-MM-DD HH:mm:ss');
            console.debug(`formatNow=${formatNow}`);
            return formatNow;
        } catch (error) {
            throw new Error(error.message);
        }
    }
}
