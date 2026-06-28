import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { MainLayoutComponent } from './shared/layout/main-layout/main-layout.component';
import { DashboardComponent } from './features/dashboard/dashboard/dashboard.component';
import { authGuard } from './core/guards/auth.guard';
import { DemandeListComponent } from './features/demandes/demande-list/demande-list.component';
import { DemandeFormComponent } from './features/demandes/demande-form/demande-form.component';
import { CvListComponent } from './features/cvs/cv-list/cv-list.component';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'demandes', component: DemandeListComponent },
{ path: 'demandes/nouvelle', component: DemandeFormComponent },
{ path: 'demandes/:id/modifier', component: DemandeFormComponent },
{ path: 'cvs', component: CvListComponent }
    ]
  },

  { path: '**', redirectTo: '/login' }
];