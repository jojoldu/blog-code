import {Service} from "typedi";
import {NodeTemplate} from "../../config/database/NodeTemplate";
import {LecturesRequest} from "../../controller/lecture/dto/LecturesRequest";
import {LecturesItem} from "./dto/LecturesItem";

@Service()
export class LectureRepository {

    constructor(private nodeTemplate: NodeTemplate) {}

    async getLectures (param: LecturesRequest) {
        const queryBody = this.getLecturesQuery(param);
        const count = await this.nodeTemplate.query(`SELECT COUNT(1) ${queryBody}`);
        const items = await this.nodeTemplate.query(`SELECT * ${queryBody}`);
        return {
            items: items.map(d => new LecturesItem(d)),
            count: parseInt(count[0].count)
        }
    }

    private getLecturesQuery (param: LecturesRequest) {
        const condition = param.getWhereCondition();
        return `FROM lecture ${condition} ${param.getPageQuery()}`;
    }

    async getLecture (lectureId: number) {
        const items = await this.nodeTemplate.query(`SELECT * FROM lecture WHERE id = '${lectureId}'`);
        return items[0];
    }
}
