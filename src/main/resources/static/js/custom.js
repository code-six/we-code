document.addEventListener('DOMContentLoaded', function () {
    const showModal = (message) => {
        document.getElementById('updateMessage').textContent = message;
        $('#updateModal').modal('show');
    };

    const updateUI = (elementId, newValue) => {
        document.getElementById(elementId).textContent = newValue;
    };

    const handleFormSubmission = (form, actionUrl, elementIdToUpdate, inputname) => {
        form.addEventListener('submit', function (event) {
            event.preventDefault();
            fetch(actionUrl, {
                method: 'POST',
                body: new URLSearchParams(new FormData(form)),
                headers: {
                    'X-CSRF-TOKEN': document.querySelector('[name="_csrf"]').value
                }
            }).then(response => {
                if (response.headers.get("Logout") === "true") {
                    setTimeout(() => {
                        window.location.href = '/';
                    }, 5000); // 5초 후에 루트 페이지로 리다이렉트
                }
                return response.text();
            })
                .then(data => {
                    showModal(data);  // 모달 창 표시
                    if (elementIdToUpdate) {
                        const newValue = form.elements[inputname].value;
                        updateUI(elementIdToUpdate, newValue);  // UI 업데이트
                    }
                })
                .catch(error => showModal('업데이트 실패: ' + error));
        });
    };

    handleFormSubmission(document.getElementById('nicknameEditForm'), '/user/mypage/updateNickname', 'nicknameDisplay', 'nickname');
    handleFormSubmission(document.getElementById('emailEditForm'), '/user/mypage/updateEmail', 'emailDisplay', 'email');
    handleFormSubmission(document.getElementById('passwordChangeForm'), '/user/mypage/updatePassword', null, 'password'); // 비밀번호는 표시되지 않음

    document.getElementById('editNicknameBtn').addEventListener('click', () => {
        document.getElementById('nicknameEditForm').style.display = 'block';
    });
    document.getElementById('cancelNicknameBtn').addEventListener('click', () => {
        document.getElementById('nicknameEditForm').style.display = 'none';
    });
    document.getElementById('editEmailBtn').addEventListener('click', () => {
        document.getElementById('emailEditForm').style.display = 'block';
    });
    document.getElementById('cancelEmailBtn').addEventListener('click', () => {
        document.getElementById('emailEditForm').style.display = 'none';
    });
    document.getElementById('changePasswordBtn').addEventListener('click', () => {
        document.getElementById('passwordChangeForm').style.display = 'block';
    });

    // 탭을 클릭했을 때 이벤트 핸들링
    $('.nav-tabs a').on('click', function (e) {
        e.preventDefault();
        $(this).tab('show');
    });

    // 모달 닫기 버튼 이벤트 재확인
    $('.btn[data-dismiss="modal"]').on('click', function () {
        $('#updateModal').modal('hide');
    });

});
