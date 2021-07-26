import {BaseEntity} from "../BaseEntity";

export class StudentLectureMap extends BaseEntity {
    readonly studentId: number;
    readonly lectureId: number;

    constructor(studentId: number, lectureId: number) {
        super();
        this.studentId = studentId;
        this.lectureId = lectureId;
    }

    static register(studentId: number, lectureId: number): StudentLectureMap {
        return new StudentLectureMap (studentId, lectureId);
    }
}
