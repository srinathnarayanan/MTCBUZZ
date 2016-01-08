package com.example.mtcbuzz;

import android.view.View;
import android.widget.TextView;

import com.myfirstapp.cardsui.*;

public class MyCard extends RecyclableCard {

	public MyCard(String title, String description){
		super(title, description);
	}

	@Override
	protected int getCardLayoutId() {
		return R.layout.card_ex;
	}

	@Override
	protected void applyTo(View convertView) {
		((TextView) convertView.findViewById(R.id.title)).setText(title);
		((TextView) convertView.findViewById(R.id.description)).setText(description);
	}
}
