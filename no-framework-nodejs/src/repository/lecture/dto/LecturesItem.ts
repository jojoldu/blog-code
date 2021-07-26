import {LectureCategory} from "../../../entity/lecture/LectureCategory";

export class LecturesItem {
    readonly id: number;
    readonly name: string;
    readonly category: LectureCategory;
    readonly price: number;
    readonly studentCount: number;
    readonly instructorName: string;
    readonly createdAt: string;

    constructor(queryResult) {
        this.id = queryResult.id;
        this.name = queryResult.name;
        this.category = queryResult.category;
        this.price = queryResult.price;
        this.studentCount = queryResult.student_count;
        this.instructorName = queryResult.instructor_name;
        this.createdAt = queryResult.created_at;
    }

}
