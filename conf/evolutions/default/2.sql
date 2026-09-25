# --- !Ups

INSERT INTO sdec_staff (pid, name)
VALUES
    (1001, 'John Test'),
    (1002, 'James Brown'),
    (1003, 'Jane Doe'),
    (1004, 'Mary Lamb');

INSERT INTO sdec_team (srs_name, is_task_based)
VALUES ( 'child_benefit', true),
       ( 'vat', false);

INSERT INTO staff_role ( staff_id, team_id, srs_role)
VALUES (1, 1, 'caseworker'),
       (1, 2, 'supervisor'),
       (2, 1, 'caseworker'),
       (3, 2, 'supervisor'),
       (4, 1, 'supervisor');

INSERT INTO sdec_recipient (internal_id, first_name, last_name, email, phone_number, nino)
VALUES ('12345', 'John', 'Smith', 'user@test.com', NULL, 'WM111111D');

INSERT INTO sdec_thread (reference,
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
VALUES (
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

       (
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