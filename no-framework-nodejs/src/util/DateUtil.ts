import dayjs from "dayjs";

export class DateUtil {
    static toString(date: Date):string {
        return dayjs(date).format("YYYY-MM-DD HH:mm:ss");
    }
}
