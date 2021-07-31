import {Service} from "typedi";
import {StudentRepository} from "../../src/repository/student/StudentRepository";
import {Student} from "../../src/entity/student/Student";

@Service()
export class TestStudentCreator {
    constructor(private studentRepository: StudentRepository) {
    }

    async create(name: string): Promise<number>{
        return await this.studentRepository.insert(Student.signup(name, "test@inflearn.com"));
    }
}
