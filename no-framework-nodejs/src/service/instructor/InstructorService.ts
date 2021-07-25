import {Service} from "typedi";
import {SampleRepository} from "../../repository/SampleRepository";

@Service()
export class InstructorService {

    constructor(private sampleRepository: SampleRepository) {
    }

    getInstructors() {
        return ['a','b','c'];
    }

    async getNow() {
        return await this.sampleRepository.now();
    }

}
