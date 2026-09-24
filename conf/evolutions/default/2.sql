# --- !Ups

INSERT INTO sdec_staff (id, pid, name)
VALUES
    (1, 1001, 'John Test'),
    (2, 1002, 'James Brown'),
    (3, 1003, 'Jane Doe'),
    (4, 1004, 'Mary Lamb');

INSERT INTO sdec_team (id, srs_name, is_task_based)
VALUES (1, 'child_benefit', true),
       (2, 'vat', false);

INSERT INTO staff_role (id, staff_id, team_id, srs_role)
VALUES (1, 1, 1, 'caseworker'),
       (2, 1, 2, 'supervisor'),
       (3, 2, 1, 'caseworker'),
       (4, 3, 2, 'supervisor'),
       (5, 4, 1, 'supervisor');

INSERT INTO sdec_recipient (id, internal_id, first_name, last_name, email, phone_number, nino)
VALUES (1, '12345', 'John', 'Smith', 'user@test.com', NULL, 'WM111111D');

INSERT INTO sdec_thread (id,
                         reference,
                         status,
                         created_by,
                         created_timestamp,
                         last_updated_timestamp,
                         thread_expiry_date,
                         case_reference,
                         recipient_id,
                         email,
                         nino,
                         message,
                         required_by)
VALUES (1,
        'THREAD1000AA',
        'Active',
        1,
        '2026-09-01 09:30:00',
        '2026-09-01 10:15:00',
        '2026-10-01',
        'CASE-100001',
        1,
        'john.smith@example.com',
        'AA123456A',
        'Where are the files?',
        NULL),

       (2,
        'THREAD2000BB',
        'Draft',
        3,
        '2026-09-02 14:00:00',
        '2026-09-02 14:00:00',
        '2026-10-02',
        'CASE-100002',
        NULL,
        'jane.doe@example.com',
        'BB654321B',
        'Please send the requested files',
        '2027-01-31');


# --- !Downs

DELETE
FROM sdec_thread;
DELETE
FROM sdec_recipient;
DELETE
FROM staff_role;
DELETE
FROM sdec_team;
DELETE
FROM sdec_staff;