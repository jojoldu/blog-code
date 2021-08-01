import {Service} from "typedi";
import {NodePgTemplate} from "../../config/database/NodePgTemplate";
import {toInsertQuery, toUpdateQuery} from "../../config/orm/objectToSql";
import {transform} from "../../config/orm/transform";
import {Student} from "../../entity/student/Student";
import {BaseRepository} from "../BaseRepository";

@Service()
export class StudentLectureMapRepository extends BaseRepository {

    constructor(nodePgTemplate: NodePgTemplate) {
        super(nodePgTemplate);
    }

}
