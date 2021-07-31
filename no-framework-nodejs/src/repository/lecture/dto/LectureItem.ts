import {LectureCategory} from "../../../entity/lecture/LectureCategory";
import {LectureStudentItem} from "./LectureStudentItem";
import {Lecture} from "../../../entity/lecture/Lecture";
import {DateUtil} from "../../../util/DateUtil";

export class LectureItem {
    readonly name: string;
    readonly description: string;
    readonly category: LectureCategory;
    readonly price: number;
    readonly studentCount: number;
    readonly createdAt: string;
    readonly updatedAt: string;
    readonly students: LectureStudentItem[] = [];

    constructor(lecture: Lecture, studentQueryResult) {
        this.name = lecture.name;
        this.description = lecture.description;
        this.category = lecture.category;
        this.price = lecture.price;
        this.studentCount = lecture.studentCount;
        this.createdAt = DateUtil.toString(lecture.createdAt);
        this.updatedAt = DateUtil.toString(lecture.updatedAt);

        if(studentQueryResult) {
            this.addStudents(studentQueryResult);
        }
    }

    addStudents(students: any[]): void {
        for (const student of students) {
            this.students.push(new LectureStudentItem(student));
        }
    }

}
