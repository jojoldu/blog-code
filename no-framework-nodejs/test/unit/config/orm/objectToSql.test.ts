import {LectureCategory} from "../../../../src/entity/lecture/LectureCategory";
import {Lecture} from "../../../../src/entity/lecture/Lecture";
import {
    toDeleteQuery,
    toInsertQuery,
    toSelectAllQuery,
    toSelectOneQuery,
    toUpdateQuery
} from "../../../../src/config/orm/objectToSql";

describe('objectToSql', () => {

    it('toSelectAllQuery', () => {
        const result = toSelectAllQuery(Lecture);
        expect(result).toBe('SELECT * FROM lecture');
    });

    it('toSelectAllQuery', () => {
        const result = toSelectOneQuery(Lecture, 1);
        expect(result).toBe('SELECT * FROM lecture WHERE id=1');
    });

    it('insert query', () => {
        const name = "name1";
        const description = "description1";
        const category = LectureCategory.WEB;
        const price = 1000;
        const instructorId = 1;
        const lecture = Lecture.create(name, description, category, price, instructorId);

        const result = toInsertQuery(lecture);

        expect(result).toContain('INSERT INTO lecture');
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

        expect(result).toContain('UPDATE lecture SET');
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
