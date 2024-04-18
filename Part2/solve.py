import numpy as np
from scipy.optimize import fsolve

def equations(vars):
    a, b, c, d, e, f = vars
    g = 0.9 # Discount factor
    eq1 = a - (0.4*0.7*(2+g*b) + 0.4*0.3*(-1+g*a) + 0.6*0.5*(-2+g*e) + 0.6*0.5*(3+g*c))
    eq2 = b - (0.4*0.1*(4+g*d) + 0.4*0.9*(1+g*b) + 0.6*1*(2+g*c))
    eq3 = c - (0.4*1*(2+g*b) + 0.6*0.7*(3+g*f) + 0.6*0.3*(-3+g*e))
    eq4 = d - (0.4*0.2*(-4+g*a) + 0.4*0.8*(3+g*d) + 0.6*1*(2+g*f))
    eq5 = e - (0.4*1*(2+g*b) + 0.6*1*(-2+g*e))
    eq6 = f - (0.4*1*(5+g*d) + 0.6*0.2*(3+g*f) + 0.6*0.8*(-4+g*e))
    return [eq1, eq2, eq3, eq4, eq5, eq6]

# Initial guesses for [a, b, c, d, e, f]
initial_guesses = [0, 0, 0, 0, 0, 0]

# Solve the equations
solution = fsolve(equations, initial_guesses)

# Printing the results with state names
state_names = ['v_π(s_0) | Stressed ', 'v_π(s_1) | Content ', 'v_π(s_2) | Motivated ', 'v_π(s_3) | Joyful', 'v_π(s_4) | Fatigued', 'v_π(s_5) | Focused']
print("The solution is:")
for name, value in zip(state_names, solution):
    print(f"{name} -> {value:.6f}")