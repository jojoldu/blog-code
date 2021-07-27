import {BaseEntity} from "../BaseEntity";
import {LecturePublishStatus} from "./LecturePublishStatus";
import {LectureCategory} from "./LectureCategory";
import {StudentLectureMap} from "../student/StudentLectureMap";

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

    static byJson (json) {
        const lecture = new Lecture();
        lecture.id = json.id;
        lecture.createdAt = json.created_at;
        lecture.updatedAt = json.updated_at;
        lecture.name = json.name;
        lecture.description = json.description;
        lecture.category = json.category;
        lecture.price = json.price;
        lecture.studentCount = json.student_count;
        lecture.publishStatus = json.publish_status;
        lecture.instructorId = json.instructor_id;
        return lecture;
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

    publish(): void {
        this.publishStatus = LecturePublishStatus.PUBLIC;
    }

    isPublish(): boolean {
        return this.publishStatus == LecturePublishStatus.PUBLIC;
    }

    register(studentId: number): StudentLectureMap {
        if(!this.isPublish()) {
            return null;
        }

        this.studentCount++;
        return StudentLectureMap.register(studentId, this.id);
    }




}
