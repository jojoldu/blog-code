import {transform} from "../../../../src/config/orm/transform";
import {Student} from "../../../../src/entity/student/Student";
import {Instructor} from "../../../../src/entity/instructor/Instructor";

describe('transform', () => {
    it('json to Student', () => {
        const json = {
            "id": 1,
            "name": "test1",
            "email": "test2"
        }
        const result = transform(json, Student);

        expect(result.id).toBe(1);
        expect(result.name).toBe("test1");
        expect(result.email).toBe("test2");
    });

    it('json to Instructor', () => {
        const json = {
            "id": 1,
            "name": "test1",
            "company_name": "test2"
        }
        const result = transform(json, Instructor);

        expect(result.id).toBe(1);
        expect(result.name).toBe("test1");
        expect(result.companyName).toBe("test2");
    });

});
