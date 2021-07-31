import {Lecture} from "../../src/entity/lecture/Lecture";
import {LectureCategory} from "../../src/entity/lecture/LectureCategory";
import {Service} from "typedi";
import {LectureRepository} from "../../src/repository/lecture/LectureRepository";

@Service()
export class TestLectureCreator {
    constructor(private lectureRepository: LectureRepository) {
    }

    async create(name: string): Promise<number>{
        const lecture = Lecture.create(
            name,
            "",
            LectureCategory.WEB,
            1000,
            1
        );

        lecture.publish();
        return await this.lectureRepository.insert(lecture);
    }
}
