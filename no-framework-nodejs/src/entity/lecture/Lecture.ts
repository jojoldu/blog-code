import {BaseEntity} from "../BaseEntity";
import {LecturePublishStatus} from "./LecturePublishStatus";
import {LectureCategory} from "./LectureCategory";

export class Lecture extends BaseEntity {
    name: string;
    description: string;
    category: LectureCategory;
    price: number;
    studentCount: number;
    publishStatus: LecturePublishStatus;
    instructorId: number;

    constructor() {
        super();
    }

    static create(name: string, description: string, category: LectureCategory, price: number, instructorId: number): Lecture {
        const lecture = new Lecture();
        lecture.name = name;
        lecture.description = description;
        lecture.category = category;
        lecture.price = price;
        lecture.studentCount = 0;
        lecture.publishStatus = LecturePublishStatus.PRIVATE;
        lecture.instructorId = instructorId;
        return lecture;
    }

    updateContent(name: string, description: string, category: LectureCategory, price: number) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
    }

    publish (): void {
        this.publishStatus = LecturePublishStatus.PUBLIC;
    }

    register (): void {
        this.studentCount++;
    }




}
