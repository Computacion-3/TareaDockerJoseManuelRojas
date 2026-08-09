import bcrypt

# Generate hash with explicit $2a$ prefix (like Java BCrypt)
password = b'admin123'
# Bcrypt gensalt might produce $2b$, let's try different approach
# Using rounds=10 like Java's BCryptPasswordEncoder default
salt = bcrypt.gensalt(rounds=10, prefix=b'2a')
h = bcrypt.hashpw(password, salt)
print("Hash (2a):", h.decode())

# Also verify
if bcrypt.checkpw(password, h):
    print("Verification: OK")