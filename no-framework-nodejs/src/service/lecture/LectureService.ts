import {Service} from "typedi";
import {LectureRepository} from "../../repository/lecture/LectureRepository";
import {LecturesRequest} from "../../controller/lecture/dto/LecturesRequest";
import {Page} from "../Page";
import {NodeTemplate} from "../../config/database/NodeTemplate";

@Service()
export class LectureService {

    constructor(
        private lectureRepository: LectureRepository,
        private nodeTemplate: NodeTemplate
        ) {
    }

    async getLectures (param: LecturesRequest) {
        const {items, count} = await this.lectureRepository.getLectures(param);
        return new Page(count, param.getPageSize(), items);
    }

    async getLecture(lectureId: number) {
        const item = await this.lectureRepository.getLecture(lectureId);
    }

    async register (studentId: number, lectureId: number) {
        const lecture = await this.lectureRepository.findEntity(lectureId);
        const result = lecture.register(studentId);

        const poolClient = await this.nodeTemplate.startTransaction();
        await this.lectureRepository.register(result);
        await this.nodeTemplate.commit(poolClient);
    }

}
