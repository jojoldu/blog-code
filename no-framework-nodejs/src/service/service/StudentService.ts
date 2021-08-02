import {Service} from "typedi";
import {StudentRepository} from "../../repository/student/StudentRepository";
import {StudentCreateRequest} from "../../controller/student/dto/StudentCreateRequest";

@Service()
export class StudentService {

    constructor(private studentRepository: StudentRepository) {
    }


    async signup(param: StudentCreateRequest) {
        const isExist = await this.studentRepository.exist(param.email);
        if(isExist) {
            throw new Error(`해당 Email은 이미 등록된 사용자입니다. email=${param.email}`);
        }

        await this.studentRepository.insert(param.toEntity());
    }

}
