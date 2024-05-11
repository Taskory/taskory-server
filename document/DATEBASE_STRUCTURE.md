# Datebase Structure

1. **Schedule 테이블**:
   - id: 일정의 고유 식별자
   - parent_user: 일정을 생성한 사용자의 식별자
   - name: 일정의 이름
   - tag: 일정에 대한 태그 또는 카테고리
   - hashtag: 일정의 해시태그
   - description: 일정에 대한 추가 설명 또는 메모
   - start_date_time: 일정 시작 일시
   - end_date_time: 일정 종료 일시
   - location: 일정이 진행되는 장소
   - child_task_set: 해당 일정과 연관된 작업 집합의 식별자 (NULL 허용)
2. **Task Set 테이블**:
   - id: 작업 집합의 고유 식별자
   - parent_user: 작업 집합을 생성한 사용자의 식별자
   - name: 작업 집합의 이름
   - hashtag: 작업 집합의 해시태그
   - description: 작업 집합에 대한 추가 설명 또는 메모
   - parent_schedule: 해당 작업 집합과 연관된 일정의 식별자 (NULL 허용)
3. **Task 테이블**:
   - id: 작업의 고유 식별자
   - parent_user: 작업을 생성한 사용자의 식별자
   - name: 작업의 이름
   - parent_task_set: 해당 작업과 연관된 작업 집합의 식별자 (NULL 허용)
   - tag: 작업에 대한 태그 또는 카테고리
   - hashtag: 작업의 해시태그
   - description: 작업에 대한 추가 설명 또는 메모
   - status: 작업의 상태 (Todo, InProgress, Done 등)
   - datetime_start: 작업의 시작 일시
   - parent_task: 작업의 부모 작업의 식별자 (NULL 허용)
4. **Routine 테이블**:
   - id: 루틴의 고유 식별자
   - parent_user: 루틴을 생성한 사용자의 식별자
   - name: 루틴의 이름
   - tag: 루틴에 대한 태그 또는 카테고리
   - hashtag: 루틴의 해시태그
   - description: 루틴에 대한 추가 설명 또는 메모
   - days: 루틴이 발생하는 요일 또는 주기
5. **Tag 테이블**:
   - id: 태그의 고유 식별자
   - parent_user: 태그를 생성한 사용자의 식별자
   - name: 태그의 이름
   - hashtag: 태그의 해시태그
   - description: 태그에 대한 설명
6. **Hashtag 테이블**:
   - id: 해시태그의 고유 식별자
   - parent_user: 해시태그를 생성한 사용자의 식별자
   - name: 해시태그의 이름

7. User 테이블

   - id

   - username

   - password

