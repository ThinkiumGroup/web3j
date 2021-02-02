pragma experimental ABIEncoderV2;
pragma solidity ^0.5.0;

contract Business {

    struct BusinessObj {
        string dataNo;
        string data;
        //string status;
    }

    uint64 private dataNum = 0;
    uint64 private historyNum = 0;
    address public owner;
    mapping(uint64 => bool) private existDatas;
    mapping(uint64 => BusinessObj) private objects;
    mapping(string => uint64) private id2record;

    event NewObject(string dataNo);
    event ModifyObject(string dataNo);


    constructor() public{
        owner = msg.sender;
    }

    function isRecordExist(uint64 recordId) public view returns (bool isIndeed) {
        return (existDatas[recordId] == true);

    }

    function isDataExist(string memory dataNo) public view returns (bool isIndeed) {
        uint64 recordId;
        recordId = id2record[dataNo];
        return (existDatas[recordId] == true);

    }

    function createObj(BusinessObj memory bObj) public returns (uint64 totalNum){
        require(msg.sender == owner);
        bool dataExist = isDataExist(bObj.dataNo);
        require(dataExist == false);

        id2record[bObj.dataNo] = historyNum + 1;
        objects[historyNum + 1] = bObj;

        uint64 recordId;
        recordId = id2record[bObj.dataNo];
        existDatas[recordId] = true;
        emit NewObject(bObj.dataNo);
        dataNum++;
        historyNum++;
        return historyNum;
    }

    function getAllObjs(uint64 _offset, uint64 _limit) public view returns (uint64 totalNum, uint64 offset, uint64 limit, BusinessObj[] memory objs){
        require(msg.sender == owner);
        require(_limit >= 1 && _limit <= 100);
        BusinessObj[] memory _objs;
        if (dataNum < _offset + _limit - 1) {
            _objs = new BusinessObj[](dataNum - _offset + 1);
        }

        else {
            _objs = new BusinessObj[](_limit);
        }

        uint64 curNum = 0;
        uint64 curLimit = 0;
        for (uint64 num = 1; num <= historyNum; num++) {
            if (existDatas[num]) {
                curNum++;
                if (curNum >= _offset) {

                    _objs[curLimit] = objects[num];
                    curLimit++;
                    if (curLimit >= _limit) {
                        break;
                    }
                }
            }
        }
        return (dataNum, _offset, _limit, _objs);
    }

    function getObjById(string memory dataNo) public view returns (BusinessObj memory obj){
        require(msg.sender == owner || isDataExist(dataNo));
        uint64 recordId = id2record[dataNo];
        BusinessObj storage newObject = objects[recordId];
        return newObject;
    }

    function updateObj(BusinessObj memory bObj, string memory dataNo) public returns (bool result){
        require(msg.sender == owner || isDataExist(dataNo));
        uint64 recordId = id2record[dataNo];
        objects[recordId] = bObj;
        existDatas[recordId] = true;
        delete id2record[dataNo];
        id2record[bObj.dataNo] = recordId;
        emit ModifyObject(bObj.dataNo);

        return true;
    }

    function deleteObj(string memory dataNo) public returns (bool result){

        require(msg.sender == owner || isDataExist(dataNo));
        uint64 recordId = id2record[dataNo];
        delete objects[recordId];
        delete existDatas[recordId];
        delete id2record[dataNo];

        if (dataNum >= 1)
            dataNum = dataNum - 1;
        return true;
    }

    function getObjsNum() public view returns (uint64 totalNum){
        return (dataNum);
    }
}