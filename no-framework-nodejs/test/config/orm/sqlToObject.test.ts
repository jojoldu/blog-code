import {transform} from "../../../src/config/orm/sqlToObject";
import {Student} from "../../../src/entity/student/Student";

describe('sqlToObject', () => {
    it('json to Student', () => {
        const json = {
            "id": 1,
            "name": "test1",
            "email": "test2"
        }
        const result = transform(json, Student);

        expect(result.name).toBe("test1");
    });


});
