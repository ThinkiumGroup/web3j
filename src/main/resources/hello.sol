pragma solidity >= 0.5.0;

contract HelloWorld {
    uint256 age = 17;
    string nickname = "Hello";

    function getAge() public view returns (uint256 data){
        return age;
    }

    function getNickname() public view returns (string memory data){
        return nickname;
    }

    function setNickname(string memory data) public {
        nickname = data;
    }
}