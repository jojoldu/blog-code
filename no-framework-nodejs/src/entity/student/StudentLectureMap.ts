import {BaseEntity} from "../BaseEntity";

export class StudentLectureMap extends BaseEntity {
    studentId: number;
    lectureId: number;

    constructor() {
        super();
    }

    static register(studentId: number, lectureId: number): StudentLectureMap {
        const studentLectureMap = new StudentLectureMap ();
        studentLectureMap.studentId = studentId;
        studentLectureMap.lectureId = lectureId;
        return studentLectureMap;
    }
}
