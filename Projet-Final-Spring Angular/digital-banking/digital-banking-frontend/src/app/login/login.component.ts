import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {

  loginFormGroup!: FormGroup;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loginFormGroup = this.fb.group({
      username: this.fb.control('', Validators.required),
      password: this.fb.control('', Validators.required)
    });
  }

  handleLogin(): void {
    const username = this.loginFormGroup.value.username;
    const password = this.loginFormGroup.value.password;
    this.authService.login(username, password).subscribe({
      next: (data) => {
        this.authService.loadProfile(data);
        this.router.navigateByUrl('/customers');
      },
      error: () => {
        this.errorMessage = "Nom d'utilisateur ou mot de passe incorrect.";
      }
    });
  }
}
