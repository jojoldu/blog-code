import {EntityProperty} from "../../../src/config/orm/EntityProperty";
import dayjs = require("dayjs");
import {LectureCategory} from "../../../src/entity/lecture/LectureCategory";

describe('EntityProperty', () => {
    it('getColumnValue Date', () => {
        const date = dayjs('2021-07-17 17:01:02', 'YYYY-MM-DD HH:mm:ss').toDate();
        const entity = new EntityProperty('date', date);
        const result = entity.getColumnValue();

        expect(result).toBe("'2021-07-17 17:01:02'");
    });

    it('getColumnValue string', () => {
        const entity = new EntityProperty('name', 'value');
        const result = entity.getColumnValue();

        expect(result).toBe("'value'");
    });

    it('getColumnValue enum', () => {
        const entity = new EntityProperty('category', LectureCategory.WEB);
        const result = entity.getColumnValue();

        expect(result).toBe("'WEB'");
    });

    it('getColumnValue boolean', () => {
        const entity = new EntityProperty('flag', true);
        const result = entity.getColumnValue();

        expect(result).toBe(true);
    });

    it('getColumnValue number', () => {
        const entity = new EntityProperty('id', 1);
        const result = entity.getColumnValue();

        expect(result).toBe(1);
    });
});
