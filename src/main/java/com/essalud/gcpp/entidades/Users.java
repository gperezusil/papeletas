package com.essalud.gcpp.entidades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "Users")
public class Users extends BaseEntity implements UserDetails{
	
	private String username;
	private String password;
	private String photoUri;
	private boolean hasPhoto;
	private String displayName;
	private String area;
	private String regimen;
	private String subtitle;
	private String lastPassword;
	private int loginTries;
	private String recoveryCode;

	private String gender;
	private boolean acceptedPolicies;
	private boolean credentialExpired;
	
	// AUTHORITIES: BACK PREAUTHORIZED
	@JsonIgnore
	@OneToMany(
			fetch = FetchType.EAGER,
	        mappedBy = "user",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	private List<UserFunctions> roles = new ArrayList<>();

	
	//----------------------- FUNCIONES
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();		
		roles.forEach(r -> authorities.add( new SimpleGrantedAuthority( r.getRole()) ) );
		
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return enable;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return enable;
	}
		
	
	@Override
	public boolean isCredentialsNonExpired() {
		return !credentialExpired;
	}
	
	@Override
	public boolean isEnabled() {
		return enable;
	}
	
	@Override
	public String getPassword() {
		return password;
	}	

	
	@Override
	public String getUsername() {
		return username;
	}
	
}
