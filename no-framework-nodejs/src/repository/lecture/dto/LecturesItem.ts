import {LectureCategory} from "../../../entity/lecture/LectureCategory";

export class LecturesItem {
    readonly id: number;
    readonly name: string;
    readonly category: LectureCategory;
    readonly price: number;
    readonly studentCount: number;
    readonly instructorName: string;
    readonly createdAt: string;
}
