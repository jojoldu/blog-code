import {Service} from "typedi";
import {BaseResponse} from "../BaseResponse";
import {StudentService} from "../../service/service/StudentService";
import {StudentCreateRequest} from "./dto/StudentCreateRequest";

@Service()
export class StudentController {

    constructor(private studentService: StudentService) {}

    async signup(reqBody) {
        try {
            const result = await this.studentService.signup(StudentCreateRequest.byReqBody(reqBody));
            return BaseResponse.OK(result);
        } catch (e) {
            console.error(`signup Error=${reqBody}`, e);
            return BaseResponse.ERROR(e);
        }
    }
}
