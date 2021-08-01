import {Service} from "typedi";
import {NodePgTemplate} from "../../config/database/NodePgTemplate";
import {BaseRepository} from "../BaseRepository";
import {StudentLectureMap} from "../../entity/student/StudentLectureMap";

@Service()
export class StudentLectureMapRepository extends BaseRepository<StudentLectureMap> {

    constructor(nodePgTemplate: NodePgTemplate) {
        super(nodePgTemplate, StudentLectureMap);
    }

}
