import {Service} from "typedi";
import {LectureRepository} from "../../repository/lecture/LectureRepository";
import {LecturesRequest} from "../../controller/lecture/dto/LecturesRequest";
import {Page} from "../Page";
import {StudentLectureMap} from "../../entity/student/StudentLectureMap";

@Service()
export class LectureService {

    constructor(private lectureRepository: LectureRepository) {
    }

    async getLectures (param: LecturesRequest) {
        const {items, count} = await this.lectureRepository.getLectures(param);
        return new Page(count, param.getPageSize(), items);
    }

    async getLecture(lectureId: number) {
        const item = await this.lectureRepository.getLecture(lectureId);
    }

    async register (studentId: number, lectureId: number) {
        return await this.lectureRepository.register(StudentLectureMap.register(studentId, lectureId));
    }

}
