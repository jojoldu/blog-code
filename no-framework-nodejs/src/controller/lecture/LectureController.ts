import {Service} from "typedi";
import {BaseResponse} from "../BaseResponse";
import {LectureService} from "../../service/lecture/LectureService";
import {LecturesRequest} from "./dto/LecturesRequest";

@Service()
export class LectureController {

    constructor(private lectureService: LectureService) {
    }

    async getLectures(reqParams) {
        try {
            const params = LecturesRequest.byQueryParam(reqParams);
            const body = await this.lectureService.getLectures(params);
            return BaseResponse.OK(body);
        } catch (e) {
            return BaseResponse.ERROR(e);
        }
    }

    async getLectureOne(reqParams) {

    }
}
