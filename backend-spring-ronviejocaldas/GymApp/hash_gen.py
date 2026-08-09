import bcrypt

password = b'admin123'

# Generate hash with $2a$ prefix
salt = bcrypt.gensalt(rounds=10, prefix=b'2a')
h = bcrypt.hashpw(password, salt)
print("Hash generated:", h.decode())

# Verify
if bcrypt.checkpw(password, h):
    print("Verification: OK - Password is admin123")
else:
    print("ERROR - Password verification failed")