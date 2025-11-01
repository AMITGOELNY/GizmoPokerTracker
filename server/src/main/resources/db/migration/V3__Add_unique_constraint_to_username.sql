-- Add unique constraint to username column in user table to prevent duplicate registrations
CREATE UNIQUE INDEX idx_user_username ON user(username);