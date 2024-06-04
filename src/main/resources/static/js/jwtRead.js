// 헤더 UI 변경으로 활용하기(로그인 전: 글쓰기 버튼 없음)
// 페이지 로드 시 JWT를 읽어옵니다.
document.addEventListener('DOMContentLoaded', function () {
    var jwtToken = localStorage.getItem('jwtToken'); // 로컬 스토리지에서 JWT 토큰 가져오기
    if (jwtToken) {
        console.log('저장된 JWT:', jwtToken);
        console.log('You are logged in!');
        document.getElementById('loggedIn').style.display = 'block'; // 로그인 상태 표시
        document.getElementById('loggedOut').style.display = 'none'; // 비로그인 상태 숨기기
        // JWT를 활용하여 필요한 작업을 수행합니다.
        // 예를 들어, 사용자 정보를 가져오거나 보호된 API 호출을 할 수 있습니다.
    } else {
        console.log('JWT가 없습니다.');
        console.log('You are not logged in!');
        document.getElementById('loggedOut').style.display = 'block';
    }
    // 로그아웃 버튼 클릭 시 처리
    document.getElementById('logoutButton').addEventListener('click', function (event) {
        event.preventDefault(); // 폼 제출 방지 - 이 코드가 없으면 users/logout으로 이동
        localStorage.removeItem('jwtToken');  // 로컬 스토리지에서 JWT 토큰 삭제
        console.log('Logged out and JWT token removed.');
        alert("로그아웃 되었습니다. \nYou have been logged out.")
        document.getElementById('loggedIn').style.display = 'none';  // 로그인 상태 숨기기
        document.getElementById('loggedOut').style.display = 'block';  // 비로그인 상태 표시
    });
});