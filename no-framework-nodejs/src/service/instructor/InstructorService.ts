import {Service} from "typedi";
import {InstructorRepository} from "../../repository/instructor/InstructorRepository";
import {InstructorCreateRequest} from "../../controller/instructor/dto/InstructorCreateRequest";

@Service()
export class InstructorService {

    constructor(private instructorRepository: InstructorRepository) {
    }

    getInstructors() {
        return ['a','b','c'];
    }

    async getNow() {
        return await this.instructorRepository.now();
    }

    async signup(param: InstructorCreateRequest) {
        const isExist = await this.instructorRepository.exist(param.email);
        if(isExist) {
            throw new Error(`해당 Email은 이미 등록된 사용자입니다. email=${param.email}`);
        }

        await this.instructorRepository.insert(param.toEntity());
    }

}
