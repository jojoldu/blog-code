import {Lecture} from "../../../../src/entity/lecture/Lecture";
import {LectureCategory} from "../../../../src/entity/lecture/LectureCategory";
import {EntityProperty} from "../../../../src/config/orm/EntityProperty";
import {EntityProperties} from "../../../../src/config/orm/EntityProperties";

describe('EntityProperties', () => {
    it('lecture의 key / value가 반환된다', () => {
        const name = "name1";
        const description = "description1";
        const category = LectureCategory.WEB;
        const price = 1000;
        const instructorId = 1;
        const lecture = Lecture.create(name, description, category, price, instructorId);

        const properties = new EntityProperties(lecture).properties;

        assertProperty(properties, "name", name);
        assertProperty(properties, "description", description);
        assertProperty(properties, "category", category);
        assertProperty(properties, "price", price);
        assertProperty(properties, "instructorId", instructorId);
    });

    it("BaseEntity의 시간값도 반환된다", () => {
        const now = new Date();
        const lecture = Lecture.create(null, null, null, 0, 0);
        lecture.renewCreatedAt(now);

        const properties = new EntityProperties(lecture).properties;

        expect(getProperty(properties, "createdAt").getTime()).toBeGreaterThanOrEqual(now.getTime());
        expect(getProperty(properties, "updatedAt").getTime()).toBeGreaterThanOrEqual(now.getTime());
    });
});

function assertProperty(properties: Array<EntityProperty>, name: string, value: any) {
    const propertyValue = getProperty(properties, name);
    expect(propertyValue).toBe(value);
}

function getProperty(properties: Array<EntityProperty>, name: string) {
    return properties.find(p => p.equals(name)).value;
}
