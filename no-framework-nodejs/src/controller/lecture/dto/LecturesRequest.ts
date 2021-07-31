import {LectureCategory} from "../../../entity/lecture/LectureCategory";
import {PageRequest} from "../../PageRequest";
import {LecturePublishStatus} from "../../../entity/lecture/LecturePublishStatus";

export class LecturesRequest extends PageRequest {
    private _name: string;
    private _lectureName: string;
    private _studentId: number;
    private _category: LectureCategory;
    private _order: string;
    private _isAsc: boolean;

    constructor() {
        super();
    }

    static byQueryParam(queryParam) {
        const result = new LecturesRequest();
        result._name = queryParam.name;
        result._lectureName = queryParam.lectureName;
        result._studentId = queryParam.studentId;
        result._category = queryParam.category;
        result._order = queryParam.order;
        result._isAsc = queryParam.isAsc;

        return result;
    }

    get name(): string {
        return this._name;
    }

    get lectureName(): string {
        return this._lectureName;
    }

    get studentId(): number {
        return this._studentId;
    }

    get category(): LectureCategory {
        return this._category;
    }

    get order(): string {
        return this._order;
    }

    get isAsc(): boolean {
        return this._isAsc;
    }

    /**
     * 원래는 HTTP Request Dto와 Repository Dto는 분리해야한다.
     * 다만, 작업 시간을 고려해서 둘을 같이 두었으나
     * 이후 엔터프라이즈 환경이 된다면 분리할 예정
     */
    getWhereCondition() {
        const statusCondition = `publish_status = '${LecturePublishStatus.PUBLIC}'`;

        if (!this._name && !this._lectureName && !this._studentId && !this._category) {
            return `WHERE ${statusCondition}`;

        }
        const condition = [statusCondition];

        if(this._name) {
            condition.push(`name = '${this._name}'`);
        }

        if(this._lectureName) {
            condition.push(`lecture_name = '${this._lectureName}'`);
        }

        if(this._category) {
            condition.push(`category = '${this._category}'`);
        }

        return `where ${condition.join(' AND ')}`
    }

    getOrderBy() {
        const asc = this._isAsc? 'ASC' : 'DESC';
        return `ORDER BY ${this._order} ${asc}`;
    }

}
