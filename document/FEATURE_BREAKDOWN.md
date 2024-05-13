# Feature breakedown

## ENG

1. **Schedule**
   - Schedule Creation
     - Functionality to add a new schedule with details such as name, tags, description, start date, end date, location, and associated tasks.
   - Schedule Modification
     - Capability to modify existing schedule details including name, tags, description, dates, and location.
   - Schedule Deletion
     - Ability to remove a schedule from the database along with its associated tasks.
   - Schedule Viewing
     - Displaying schedule details including name, tags, description, dates, location, and associated tasks.
   - API Integration
     - Integration with social accounts such as Google to synchronize schedules from those platforms into the service.
2. **Task Set**
   - Task Set Creation
     - Functionality to create a new task set with details such as name, tags, description, and association with a schedule if applicable.
     - Ability to link a task set with a schedule.
   - Task Set Modification
     - Capability to modify existing task set details such as name, tags, and description.
   - Task Set Deletion
     - Ability to delete a task set from the database along with its associated tasks.
   - Task Set Viewing
     - Displaying task set details including name, tags, description, and associated tasks.
3. **Task**
   - Task Creation
     - Functionality to add a new task with details like name, tags, description, status, start date, and association with a task set or parent task if applicable.
   - Task Modification
     - Capability to modify existing task details such as name, tags, description, status, and dates.
   - Task Deletion
     - Ability to delete a task from the database.
   - Task Viewing
     - Displaying task details including name, tags, description, status, dates, and parent task or task set association.
4. **Routine**
   - Routine Creation
     - Functionality to create a new routine with details such as name, tags, description, and recurrence pattern (days or frequency).
   - Routine Modification
     - Capability to modify existing routine details including name, tags, description, and recurrence pattern.
   - Routine Deletion
     - Ability to remove a routine from the database.
   - Routine Viewing
     - Displaying routine details including name, tags, description, and recurrence pattern.
5. **Tag**
   - Tag Creation
     - Functionality to add a new tag with a name, hashtag, and description.
   - Tag Modification
     - Capability to modify existing tag details such as name, hashtag, and description.
   - Tag Deletion
     - Ability to delete a tag from the database.
   - Tag Viewing
     - Displaying tag details including name, hashtag, and description.
     - Ability to view tasks, task sets, and schedules associated with a specific tag.
     - Dashboard feature allowing separate viewing based on tags.
6. **Hashtag**
   - Hashtag Creation
     - Automatically generating hashtags for tasks, task sets, and schedules.
   - Hashtag Modification
     - Automatically updating hashtags when tasks, task sets, or schedules are modified.
   - Hashtag Deletion
     - Checking for other occurrences of hashtags before deletion when tasks, task sets, or schedules are deleted.
   - Hashtag Viewing
     - Displaying details of hashtags including associated tasks, task sets, and schedules.

Plus. Additional Features for Post-Prototype Development:

1. **Schedule Reminder Feature**
   - Sending notifications to users at set start times to ensure they don't miss their schedules.
2. **Task Priority Setting Feature**
   - Allowing users to prioritize tasks to manage their workflow more effectively.
3. **Schedule Sharing Feature**
   - Enabling users to share schedules with others, useful for team projects or meetings.
4. **Statistics and Reports Feature**
   - Generating statistics and reports on user tasks and routines to track productivity and identify areas for improvement.
5. **Schedule Duplication Check Feature**
   - Checking for existing schedules when adding new ones to prevent duplication.
6. **Multi-Schedule Viewing Feature**
   - Providing users with a calendar view or schedule table to see multiple schedules at once.
7. **Task Template Feature**
   - Saving frequently repeated tasks as predefined templates for efficient task addition.
8. **Automatic Routine Creation Feature**
   - Automatically generating routines based on user-defined patterns for efficient management of repetitive tasks.

## KOR

1. **Schedule**
   - 일정 생성
     - 이름, 태그, 설명, 시작일 및 종료일, 위치 및 연관된 작업 등의 세부 정보로 새로운 일정 추가 기능.
   - 일정 수정
     - 이름, 태그, 설명, 일자 및 위치 등의 기존 일정 세부 정보 수정 기능.
   - 일정 삭제
     - 데이터베이스에서 일정 삭제 및 해당 일정과 관련된 작업 제거 기능.
   - 일정 보기
     - 이름, 태그, 설명, 일자, 위치 및 관련 작업 등의 일정 세부 정보 표시 기능.
   - API 연동
     - Google과 같은 Social 계정을 연동하여 해당 Social의 일정들을 서비스에 동기화 기능.
2. **Task Set**
   - 작업 집합 생성
     - 이름, 태그, 설명 및 해당하는 일정과의 연관성 등의 세부 정보로 새로운 작업 집합 생성 기능.
     - 일정(Schedule)을 연결 기능
   - 작업 집합 수정
     - 이름, 태그 및 설명 등의 기존 작업 집합 세부 정보 수정 기능.
   - 작업 집합 삭제
     - 데이터베이스에서 작업 집합 삭제 및 해당하는 작업 집합과 관련된 작업 제거 기능.
   - 작업 집합 보기
     - 이름, 태그, 설명 및 관련 작업 등의 작업 집합 세부 정보 표시 기능.
3. **Task**
   - 작업 생성
     - 이름, 태그, 설명, 상태, 시작일 및 해당하는 작업 집합 또는 부모 작업과의 연관성 등의 세부 정보로 새로운 작업 추가 기능.
   - 작업 수정
     - 이름, 태그, 설명, 상태 및 일자 등의 기존 작업 세부 정보 수정 기능.
   - 작업 삭제
     - 데이터베이스에서 작업 삭제 기능.
   - 작업 보기
     - 이름, 태그, 설명, 상태, 일자 및 부모 작업 또는 작업 집합과의 관련성 등의 작업 세부 정보 표시 기능.
4. **Routine**
   - 루틴 생성
     - 이름, 태그, 설명 및 발생 패턴(요일 또는 주기) 등의 세부 정보로 새로운 루틴 추가 기능.
   - 루틴 수정
     - 이름, 태그, 설명 및 발생 패턴 등의 기존 루틴 세부 정보 수정 기능.
   - 루틴 삭제
     - 데이터베이스에서 루틴 삭제 기능.
   - 루틴 보기
     - 이름, 태그, 설명 및 발생 패턴 등의 루틴 세부 정보 표시 기능.
5. **Tag**
   - 태그 생성
     - 이름, 해시태그 및 설명과 같은 세부 정보로 새로운 태그 추가 기능.
   - 태그 수정
     - 이름, 해시태그 및 설명 등의 기존 태그 세부 정보 수정 기능.
   - 태그 삭제
     - 데이터베이스에서 태그 삭제 기능.
   - 태그 보기
     - 태그를 선택하면 해당 태그가 들어가있는 Task, Task Set, Schedule 조회
     - 대시보드에서 태그를 이용하여 분리 조회 가능
6. **Hash Tag**
   - 해시태그 생성
     - Task, Task Set, Schedule 등에 들어가는 Hash Tag를 자동으로 생성
   - 해시태그 수정
     - Task, Task Set, Schedule 등이 수정될 때, Hash Tag가 수정 되면 자동으로 수정
   - 해시태그 삭제
     - Task, Task Set, Schedule가 삭제 될 때, 다른 Task, Task Set, Schedule 에도 Hash Tag가 존재하는지 확인한 후 없다면 삭제
   - 해시태그 보기
     - 해시태그를 선택하면 해당 해시태그가 들어가있는 Task, Task Set, Schedule 조회





Plus. Porototype 개발 이후 추가적으로 들어가면 좋을 기능

1. **일정 알림 기능 추가**

   - 사용자가 설정한 시작 시간에 알림을 보내어 사용자가 일정을 놓치지 않도록 도와주는 기능.

2. **작업 우선 순위 설정 기능**

   - 사용자가 작업에 대한 우선 순위를 지정하여 중요한 작업에 더 많은 관심을 기울일 수 있는 기능.

     - Task나 Task Set을 나열하여 사용자가 순위를 관리할 수 있도록 한다.

     - 점수를 매기는 것이 아님

       scenario

       1. task가 없는 상태
       2. Task A 생성
       3. Task B 생성
       4. Task B가 A보다 덜 중요함
       5. 우선 순위 -> Task A > Task B
       6. Task C 생성
       7. Task C는 A보다 덜 중요하고 B보다 중요함
       8. 우선 순위 Tast A > Task C > Task B

       - 이렇게 사용자가 우선순위를 상대적으로 관리할 수 있도록 관리

3. **일정 공유 기능**

   - 사용자가 다른 사용자와 일정을 공유할 수 있는 기능으로, 팀 프로젝트나 회의 등에서 유용하게 사용할 수 있습니다.

4. **통계 및 리포트 기능**

   - 사용자의 작업 및 루틴에 대한 통계 및 리포트를 생성하여 생산성을 추적하고 개선할 수 있는 기능.

5. **일정 중복 확인 기능**

   - 사용자가 새로운 일정을 추가할 때 이미 해당 기간에 다른 일정이 있는지 확인하여 중복을 방지할 수 있는 기능.

6. **다중 일정 보기 기능**

   - 사용자가 여러 일정을 한눈에 볼 수 있는 캘린더 뷰 또는 일정 테이블을 제공하는 기능.

7. **작업 템플릿 기능**

   - 자주 반복되는 작업을 미리 정의된 템플릿으로 저장하여 효율적으로 작업을 추가할 수 있는 기능.

8. **루틴 자동 생성 기능**

   - 사용자가 원하는 패턴에 따라 루틴을 자동으로 생성하여 반복적이고 규칙적인 작업을 효율적으로 관리할 수 있는 기능.



