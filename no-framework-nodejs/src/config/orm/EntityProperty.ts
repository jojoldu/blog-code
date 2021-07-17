import {camelToSnake} from "./camelToSnake";
import dayjs = require("dayjs");

export class EntityProperty {
    name: string;
    value: any;

    constructor(name: string, value: any) {
        this.name = name;
        this.value = value;
    }

    equals(name: string): boolean {
        return this.name === name;
    }

    getColumnName(): string {
        return camelToSnake(this.name);
    }

    getColumnValue() {
        const stringTypes = ['string', 'object']
        if(this.value instanceof Date) {
            return `'${dayjs(this.value).format('YYYY-MM-DD HH:mm:ss')}'`;
        }

        if (stringTypes.includes(typeof this.value)) {
            return `'${this.value}'`;
        }

        return this.value;
    }
}
