import {LectureCategory} from "../../../src/entity/lecture/LectureCategory";
import {Lecture} from "../../../src/entity/lecture/Lecture";
import {toDeleteQuery, toInsertQuery, toUpdateQuery} from "../../../src/config/orm/objectToSql";

describe('objectToSql', () => {
    it('insert query', () => {
        const name = "name1";
        const description = "description1";
        const category = LectureCategory.WEB;
        const price = 1000;
        const instructorId = 1;
        const lecture = Lecture.create(name, description, category, price, instructorId);

        const result = toInsertQuery(lecture);

        expect(result).toContain('insert into lecture');
        console.log(result);
    });

    it('update query', () => {
        const name = "name1";
        const description = "description1";
        const category = LectureCategory.WEB;
        const price = 1000;
        const instructorId = 1;
        const lecture = Lecture.create(name, description, category, price, instructorId);
        lecture.id = 1;

        const result = toUpdateQuery(lecture);

        expect(result).toContain('update lecture set');
    });

    it('delete query', () => {
        const name = "name1";
        const description = "description1";
        const category = LectureCategory.WEB;
        const price = 1000;
        const instructorId = 1;
        const lecture = Lecture.create(name, description, category, price, instructorId);
        lecture.id = 1;

        const result = toDeleteQuery(lecture);

        console.log(result);
    });

});
